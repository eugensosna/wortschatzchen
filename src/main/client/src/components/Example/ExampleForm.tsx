// src/components/Example/ExampleForm.tsx
import React, { useState } from "react";
import { Example, ExampleCreateUpdate, Word } from "../../types";
import { Box, TextField, Button, Link } from "@mui/material";
import { useNavigate, useParams } from "react-router-dom";
import { examplesApi } from "../../api/examplesApi";
import { wordsApi } from "../../api/wordsApi";
import TranslatedWordsComponent from "../common/translatedRows";


const ExampleForm: React.FC = () => {
  const { wordId, exampleId } = useParams<{ wordId: string, exampleId:string}>();
 // console.log("load edit "+ wordId+exampleId);
  const [baseWord, setBaseWord] = useState<Word>({
    name: "",
    id: 0,
  });

  // const { createExample, updateExample, loading, error } = useExamples();
  const [formData, setFormData] = useState<Example>({
    name: "",
    order: 1,
    id: 0,
    baseWordId: Number(wordId)
  });

  const navigate = useNavigate();
  

  const [loading,setLoading] = useState<boolean>(false);

  React.useEffect(() => {
    setLoading(true);

    const fetchWord = async () => {
      try {
        setLoading(true);
        const fetchedWord = await wordsApi.getById(Number(wordId));
        setBaseWord(fetchedWord);
      } catch (err) {
        console.error("Error fetching word:", err);
      } finally {
        setLoading(false);
      }
    };

    const fetchExample = async () => {
      if (exampleId !== "new") {
        setLoading(true);
        try {
          const example = await examplesApi.getById(exampleId ? Number(exampleId) : 0);
          setFormData(example);
        } catch (err) {
          console.error("Error fetching word:", err);
        } finally {
          setLoading(false);
        }
      }
    };

    if (wordId) {
      fetchWord();
    }
    if (exampleId !== "new") {
      fetchExample();
    }
  }, [wordId, exampleId]);

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
      baseWordId: baseWord.id,
      word: { id: wordId }, // Ensure the 'word' property is included
    };

    try {
      if (payload.id) {
        console.log( "update "+payload);
        await examplesApi.update(payload.id, payload);
      } else {
        const createPayload: ExampleCreateUpdate = {
          name: payload.name,
          order: payload.order,
          baseWordId: baseWord.id,
        };
        console.log( "create "+createPayload);
        await examplesApi.create(createPayload);
        
      }
    } catch (err) {
      console.error(err);
      // Error handling is done in the context
    }
    navigate(`/examples/${baseWord.id}/list`);
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit}
      sx={{ display: "flex", flexDirection: "column", gap: 2, maxWidth: 300 }}
    >
      <TextField
        label="Name"
        name="name"
        value={formData.name}
        onChange={handleChange}
        fullWidth
      />
      <TextField
        label="Order"
        name="order"
        type="number"
        value={formData.order}
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

      <Link href={`/examples/${baseWord.id}/list`}>Examples</Link>
      <TranslatedWordsComponent uuidClass={formData.uuid} />
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

export default ExampleForm;
