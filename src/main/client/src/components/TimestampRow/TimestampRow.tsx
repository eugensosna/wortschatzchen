import { useEffect, useState } from "react";
import { SubtitleFile, TimestampRow } from "../../types";
import {
  IconButton,
  Link,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material";

import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import { useNavigate, useParams } from "react-router-dom";
import { timestampRowsApi } from "../../api/timestampRowApi";
import { subtitleFilesApi } from "../../api/subtitleFilesApi";

export const TimestampRowList = () => {
  const { mainId } = useParams<{ mainId: string }>();
  const [items, setItems] = useState<TimestampRow[]>([]);

  const [mainItem, setMainItem] = useState<SubtitleFile| null>({
    name: "",
    id: 0,
    uuid: "",
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    uploadDate: "",
  });
  const navigate = useNavigate();


  useEffect(() => {
    const loadMainItems = async () => {
      try {
        const data = await subtitleFilesApi.getById(Number(mainId));
        setMainItem(data);
      } catch (err) {
        console.error(err);
      }
    };


  const loadItems = async () => {
    try {
      console.log("start load ...")
      const data = await timestampRowsApi.getAllByMainId(Number(mainId));
      setItems(data);
    } catch (err) {
      console.error(err);
    }
  };

    //loadItems();
    if (mainId){
      loadMainItems();
     
    }
    if (mainItem && mainItem.id>0 ){
      loadItems();
    }
    
  }, [mainId, mainItem]);

  const handleEdit = async (id: string) => {
    navigate(`/timestamprow/${mainId}/edit/${id}`);
  };
  const handleDeleteClick = async (id: string): Promise<void> => {
    try {
      await timestampRowsApi.delete(Number(id));
      
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <>
      <div className="m-3">
        <IconButton onClick={() => handleEdit("new")} className="mb-3">
          <AddIcon />
          Add New
        </IconButton>

        <Link href={`/timestamprow/${mainId}/edit/new`}>add</Link>
        <Typography variant="h5" className="mb-3 ml-4">
          {mainItem ? mainItem.name : "No name"} rows
        </Typography>
      </div>
      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Id</TableCell>
              <TableCell>Name</TableCell>
              <TableCell>Order</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {items.map((item) => (
              <TableRow key={item.id}>
                <TableCell>{item.id}</TableCell>
                <TableCell>{item.text}</TableCell>
                <TableCell>{item.id}</TableCell>
                <TableCell>
                  <IconButton
                    onClick={() => handleEdit(item.id.toString())}
                    color="primary"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    onClick={() => handleDeleteClick(item.id.toString())}
                    color="error"
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};
