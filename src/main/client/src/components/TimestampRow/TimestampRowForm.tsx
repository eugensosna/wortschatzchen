// src/components/Example/ExampleForm.tsx
import React, { useState } from "react";
import { SubtitleFile, TimestampRow } from "../../types";
import { Box, TextField, Button, Link } from "@mui/material";
import { useParams } from "react-router-dom";
import TranslatedWordsComponent from "../common/translatedRows";
import { subtitleFilesApi } from "../../api/subtitleFilesApi";
import { timestampRowsApi } from "../../api/timestampRowApi";

const TimestampRowForm: React.FC = () => {
  const { mainId, rowId } = useParams<{ mainId: string; rowId: string }>();

  //console.log("load edit "+ mainId+":"+rowId);
  const [mainItem, setMainItem] = useState<SubtitleFile>({
    name: "",
    id: 0,
    uploadDate: "",
  });

  // const { createExample, updateExample, loading, error } = useExamples();
  const [formData, setFormData] = useState<TimestampRow>({
    text: "",
    id: 0,
    subtitleFile: mainItem,
  });


  const [loading, setLoading] = useState<boolean>(false);

  React.useEffect(() => {
    setLoading(true);

    const fetchMainItem = async () => {
      try {
        setLoading(true);
        const fetchedWord = await subtitleFilesApi.getById(Number(mainId));
        setMainItem(fetchedWord);
      } catch (err) {
        console.error("Error fetching word:", err);
      } finally {
        setLoading(false);
      }
    };

    const fetchtItem = async () => {
      if (rowId !== "new") {
        //setLoading(true);
        try {
          const item = await timestampRowsApi.getById(
            rowId ? Number(rowId) : 0
          );
          setFormData(item);
        } catch (err) {
          console.error("Error fetching word:", err);
        } finally {
          setLoading(false);
        }
      }
    };

    if (mainId) {
      fetchMainItem();
    }
    if (rowId !== "new") {
      fetchtItem();
    }
  }, [mainId, rowId]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: name === "order" ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      ...formData,
      subtitleFile: mainItem,
      // Ensure the 'word' property is included
    };

    try {
      if (payload.id) {
        console.log("update " + payload);
        await timestampRowsApi.update(payload.id, payload);
      } else {
        const createPayload: TimestampRow = {
          text: payload.text,
          subtitleFile:mainItem,
          id:0
        };
        console.log("create " + createPayload);
        await timestampRowsApi.create(createPayload);
      }
    } catch (err) {
      console.error(err);
      // Error handling is done in the context
    }
    //navigate(`/timestamprow/${mainItem.id}/list`);
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      sx={{ display: "flex", flexDirection: "column", gap: 2, maxWidth: 300 }}
    >
      <TextField
        label="Text"
        name="text"
        value={formData.text}
        onChange={handleChange}
        fullWidth
      />
      
      <Button
        type="submit"
        variant="contained"
        color="primary"
        disabled={loading}
      >
        Submit
      </Button>

      <Link href={`/timestamprow/${mainItem.id}/list`}>List</Link>
      <TranslatedWordsComponent uuidClass={formData.uuid} textToTranslate={formData.text} baseLang={formData.subtitleFile?.baseLang?.shortName}/>
    </Box>

    // <form onSubmit={handleSubmit}>
    //   {error && <div className="alert alert-danger">{error}</div>}

    // <FormControl fullWidth>
    // <InputLabel>Example Text</InputLabel>
    // <InputText ></InputText>
    //     <Form.Control
    //       as="textarea"
    //       rows={3}
    //       value={exampleData.name}
    //       onChange={(e) => setExampleData({
    //         ...exampleData,
    //         name: e.target.value
    //       })}
    //       required
    //     />
    //   </FormControl>

    //   <Form.Group className="mb-3">
    //     <Form.Label>Order</Form.Label>
    //     <Form.Control
    //       type="number"
    //       value={exampleData.order}
    //       onChange={(e) => setExampleData({
    //         ...exampleData,
    //         order: Number(e.target.value)
    //       })}
    //       min="1"
    //       required
    //     />
    //   </Form.Group>

    //   <div className="d-flex justify-content-end">
    //     <Button
    //       variant="secondary"
    //       onClick={onClose}
    //       className="me-2"
    //       disabled={loading}
    //     >
    //       Cancel
    //     </Button>
    //     <Button
    //       variant="primary"
    //       type="submit"
    //       disabled={loading}
    //     >
    //       {loading ? 'Saving...' : (initialExample ? 'Update' : 'Create')}
    //     </Button>
    //   </div>
    // </form>
  );
};

export default TimestampRowForm;
