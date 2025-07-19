import { useEffect, useState } from "react";
import { TimestampRow } from "../../types";
import {
  Container,
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";

import EditIcon from "@mui/icons-material/Edit";
import { useNavigate, useParams } from "react-router-dom";
import { timestampRowsApi } from "../../api/timestampRowApi";
import ArrowBack from "@mui/icons-material/ArrowBack";
import ArrowForward from "@mui/icons-material/ArrowForward";
import { CenterFocusStrong } from "@mui/icons-material";
import DeleteIcon from "@mui/icons-material/Delete";
import ReadOnlyTextDisplay from "../common/ReadOnlyTextDisplay";
import TranslatedWordsComponent from "../common/translatedRows";

interface TimestampRowViewProps {
  mainId: string;
  currentPage: number;
  size: number;
  onNext: (mainId: string, nextPage: number)=>void;
  
}

export const TimestampRowView: React.FC<TimestampRowViewProps> = ({
  mainId,
  currentPage,
  size,
  onNext,
  
}) => {
  //const { mainId } = useParams<{ mainId: string }>();

  const [currentItem, setCurrenItem] = useState<TimestampRow | null>();
  const [trigger, setTigger] = useState<number>(0)

  const [totalItems, setTotalItems] = useState<number>(0);

  const navigate = useNavigate();

  useEffect(() => {
    const loadItems = async () => {
      try {
        if (mainId) {
          const response = await timestampRowsApi.getPageByMain(
            mainId,
            currentPage - 1,
            size
          );

          if (response.content.length > 0) {
            setCurrenItem(response.content[0]);
          }
          setTotalItems(response.totalElements);
        }
      } catch (err) {
        console.error(err);
      }
    };

    loadItems();
  }, [mainId, currentPage]); // total item count

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      currentPage = currentPage-1;

      navigate(`/timestamprow/${mainId}/game/${currentPage}`);
      onNext(mainId, currentPage);
      setTigger(trigger+1);
    }
  };

  const handleNextPage = () => {
    currentPage = currentPage+1;
    navigate(`/timestamprow/${mainId}/game/${currentPage}`);
    onNext(mainId, currentPage);
    setTigger(trigger+1);
    
  };
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
    <div>
      <Container maxWidth="sm">
        {currentItem && (
          <div>
            <ReadOnlyTextDisplay
              text={currentItem.text}
              label="User Information (Click to select, use copy button to copy)"
            />
            <TranslatedWordsComponent
              baseLang={currentItem.subtitleFile?.baseLang?.shortName}
              key={currentItem.uuid || ""}
              uuidClass={currentItem.uuid || ""}
              textToTranslate={currentItem.text}
            />

            <IconButton
              onClick={() => handleEdit(currentItem.id.toString())}
              color="primary"
            >
              <EditIcon />
            </IconButton>
            <IconButton
              onClick={() => handleDeleteClick(currentItem.id.toString())}
              color="error"
            >
              <DeleteIcon />
            </IconButton>
          </div>
        )}
      </Container>
      <div
        style={{ display: "flex", justifyContent: "center", marginTop: "16px" }}
      >
        <IconButton onClick={handlePreviousPage} disabled={currentPage === 1}>
          <ArrowBack />
        </IconButton>
        <IconButton
          onClick={handleNextPage}
          // disabled={currentPage * 1 >= Math.ceil(totalItems / 1)}
        >
          <ArrowForward />
        </IconButton>
      </div>
    </div>
  );
};
