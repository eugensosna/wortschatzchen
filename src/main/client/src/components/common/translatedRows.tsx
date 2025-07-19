import React, { useState, useEffect } from "react";
import {
  TableHead,
  TableRow,
  TableBody,
  TableCell,
  TableContainer,
  Table,
  IconButton,
} from "@mui/material";
import { TranslatedWord, TranslatedWordToTranslate } from "../../types";
import { TranslatedWordsApi } from "../../api/translatedWordsApi";
import GTranslateIcon from "@mui/icons-material/GTranslate";

import LanguageIcon from './LanguageIcon';

interface TranslatedWordsProps {
  baseLang?: string;
  uuidClass?: string;
  textToTranslate?: string;
}

const TranslatedWordsComponent: React.FC<TranslatedWordsProps> = ({
  uuidClass,
  textToTranslate,
  baseLang
}) => {

  console.log(uuidClass);
  const [translatedWordsList, setTranslatedWordsList] = useState<
    TranslatedWord[]
  >([]);
  const [refreshTrigger, setRefreshTrigger] = useState<number>(0);

  // Fetch data on component mount
  useEffect(() => {
   
    const loadData = async () => {
      if (uuidClass) {
        const translatedWordsData = await TranslatedWordsApi.getByUuidClass(
          uuidClass
        );
        setTranslatedWordsList(translatedWordsData);
      }
    };

    loadData();
  }, [uuidClass, textToTranslate, refreshTrigger]);

  const handleTranslateClick = async (): Promise<void> => {
    //console.log("translate...");
    if (baseLang){
      const toTranslate: TranslatedWordToTranslate = {
        from: baseLang,
        to: "",
        text: textToTranslate|| ""
      }
      if (uuidClass) {
        const items = await TranslatedWordsApi.translateByUuidClass(uuidClass, toTranslate);
        setRefreshTrigger(refreshTrigger+1);
        setTranslatedWordsList(items);
        
        
      }
    }
  };

  return (
    <div>
      <IconButton
        onClick={() => handleTranslateClick()}
        color="primary"

      >

        <GTranslateIcon/>
      </IconButton>

      <TableContainer>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Id</TableCell>
              <TableCell>Name</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {translatedWordsList.map((item) => (
              <TableRow key={item.id}>
                <TableCell>{item.id}</TableCell>
                <TableCell>  <LanguageIcon languageCode={item.targetLang.shortName} />
                {item.translatedName}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

export default TranslatedWordsComponent;
