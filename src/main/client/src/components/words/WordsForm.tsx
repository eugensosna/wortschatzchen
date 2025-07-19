// src/components/Words/WordForm.tsx
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  CircularProgress,
  Alert,
  Snackbar,
  SelectChangeEvent,
  Link,
} from "@mui/material";

import { languagesApi } from "../../api/languagesApi";
import { wordsApi } from "../../api/wordsApi";
import { Word, Language, WordCreateUpdate } from "../../types";

const WordsForm: React.FC = () => {
  
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [formData, setFormData] = useState<Partial<Word>>({
    name: "",
    //uploadDate: '',
    //file:null
  });

  const [languages, setLanguages] = useState<Language[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [loadingData, setLoadingData] = useState<boolean>(id ? true : false);
  const [error, setError] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success" as "success" | "error" | "info" | "warning",
  });

  useEffect(() => {
    const loadLanguages = async () => {
      try {
        const data = await languagesApi.getAll();
        setLanguages(data);
      } catch (err) {
        setError("Failed to load languages");
        console.error(err);
      }
    };

    const loadWord = async () => {
      if (!id) return;
      try {
        setLoadingData(true);
        const data = await wordsApi.getById(Number(id));
        setFormData(data);
      } catch (err) {
        setError("Failed to load word file");
        console.error(err);
      } finally {
        setLoadingData(false);
      }
    };

    loadLanguages();
    loadWord();
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === "duration" ? Number(value) : value,
    });
  };

  const handleLanguageChange = (e: SelectChangeEvent) => {
    const langId = Number(e.target.value);
    const selectedLang = languages.find((lang) => lang.id === langId);

    setFormData({
      ...formData,
      baseLang: selectedLang,
    });
    //setFormData({ ...formData, languageId: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      setLoading(true);

      if (id) {
        await wordsApi.update(Number(id), formData as Word);
        // await updateWord(id, formData);
        setSnackbar({
          open: true,
          message: "Subtitle file updated successfully",
          severity: "success",
        });
      } else {
        await wordsApi.create({
          ...formData,
          languageId: formData.baseLang?.id || 0,
        } as WordCreateUpdate);
        // await createWord(formData as Omit<Word, 'id' | 'createdAt' | 'updatedAt'>);
        setSnackbar({
          open: true,
          message: "Word created successfully",
          severity: "success",
        });
      }

      // Navigate back after short delay to show the success message
      setTimeout(() => navigate("/words"), 1500);
    } catch (err) {
      setError("Failed to save word file");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const closeSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  if (loadingData) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
        {id ? "Edit Word" : "Create Word"}
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <form onSubmit={handleSubmit}>
        <TextField
          label="name"
          name="name"
          value={formData.name || ""}
          onChange={handleChange}
          fullWidth
          margin="normal"
          required
        />

        <FormControl fullWidth>
          <InputLabel id="language-select-label">Base Language</InputLabel>
          <Select
            labelId="language-select-label"
            value={formData.baseLang?.id.toString() || ""}
            label="Base Language"
            onChange={handleLanguageChange}
          >
            {languages.map((lang) => (
              <MenuItem key={lang.id} value={lang.id}>
                {lang.name} ({lang.shortName})
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <Box sx={{ mt: 3, display: "flex", justifyContent: "space-between" }}>
          <Button
            variant="outlined"
            onClick={() => navigate("/words")}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            disabled={loading}
          >
            {loading ? (
              <CircularProgress size={24} />
            ) : id ? (
              "Update"
            ) : (
              "Create"
            )}
          </Button>
        </Box>
      </form>
      <Link href={`/examples/${formData.id}/list`}>Examples</Link>
      {/* <ExampleList wordId={formData.id} /> */}
      
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={closeSnackbar}
      >
        <Alert onClose={closeSnackbar} severity={snackbar.severity}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Paper>
  );
};

export default WordsForm;
