import { useEffect, useState } from "react";
import { Example, Word } from "../../types";
import { wordsApi } from "../../api/wordsApi";
import { examplesApi } from "../../api/examplesApi";
import { IconButton, Link, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material";

import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import { useNavigate, useParams } from "react-router-dom";

export const ExampleList2 = () => {
  const { wordId } = useParams<{ wordId: string }>();
  const [examples, setExamples] = useState<Example[]>([]);

  const [baseWord, setBaseWord] = useState<Word>({
	name: "",
	mean: "",
	id: 0,
	uuid: "",
	createdAt: new Date().toISOString(),
	updatedAt: new Date().toISOString(),
  });
  const navigate = useNavigate();

  const loadExamples = async () => {
	try {
	  const data = await examplesApi.getAllByWordId(Number(wordId));
	  setExamples(data);
	} catch (err) {
	  console.error(err);
	}
  };
 

  useEffect(() => {
    const loadBaseWord = async () => {
      try {
        const data = await wordsApi.getById(Number(wordId));
        setBaseWord(data);
      } catch (err) {
        console.error(err);
      }
    };

   
    loadBaseWord();
    loadExamples();
  }, [wordId, loadExamples]);

  const handleEditExample = async (id: string) => {
    navigate(`/examples/${wordId}/edit/${id}`);
  };
  const handleDeleteClick = async (id: string): Promise<void> => {
	try{
		await examplesApi.delete(Number(id));
		loadExamples();
	}
	catch(err){
		console.error(err);
	}
  };
  

  return (
    <><div className="m-3">
		  <IconButton
			  onClick={() => handleEditExample("new")}
			  className="mb-3"
		  >
			<AddIcon />
			  Add New 
			  
		  </IconButton>

		  <Link href={`/examples/${wordId}/edit/new`}>add</Link>
		  <Typography variant="h5" className="mb-3">
			  {baseWord.name} Examples
		  </Typography>
	  </div><TableContainer>
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
					  {examples.map((item) => (
						  <TableRow key={item.id}>
							  <TableCell>{item.id}</TableCell>
							  <TableCell>{item.name}</TableCell>
							  <TableCell>{item.order}</TableCell>
							  <TableCell>
								  <IconButton
									  onClick={() => handleEditExample(item.id.toString())}
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
