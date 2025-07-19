// src/components/SubtitleFiles/SubtitleFileForm.tsx
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
  IconButton,
} from "@mui/material";

import PageviewIcon from '@mui/icons-material/Pageview';

import { subtitleFilesApi } from "../../api/subtitleFilesApi";
import { languagesApi } from "../../api/languagesApi";
import { SubtitleFile, Language } from "../../types";
import ChoiceToUpload from "../common/ChoiceToUpload";
import { fileEntityApi } from "../../api/fileEntityApi";

const SubtitleFileForm: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [formData, setFormData] = useState<Partial<SubtitleFile>>({
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

    const loadSubtitleFile = async () => {
      if (!id) return;

      try {
        setLoadingData(true);
        const data = await subtitleFilesApi.getById(Number(id));
        setFormData(data);
      } catch (err) {
        setError("Failed to load subtitle file");
        console.error(err);
      } finally {
        setLoadingData(false);
      }
    };

    loadLanguages();
    loadSubtitleFile();
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
        await subtitleFilesApi.update(Number(id), formData as SubtitleFile);
        // await updateSubtitleFile(id, formData);
        setSnackbar({
          open: true,
          message: "Subtitle file updated successfully",
          severity: "success",
        });
      } else {
        await subtitleFilesApi.create(
          formData as Omit<SubtitleFile, "id" | "createdAt" | "updatedAt">
        );
        // await createSubtitleFile(formData as Omit<SubtitleFile, 'id' | 'createdAt' | 'updatedAt'>);
        setSnackbar({
          open: true,
          message: "Subtitle file created successfully",
          severity: "success",
        });
      }

      // Navigate back after short delay to show the success message
      setTimeout(() => navigate("/subtitle-files"), 1500);
    } catch (err) {
      setError("Failed to save subtitle file");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const closeSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };
  const handleParseToRows = () => {
    subtitleFilesApi.parseFile(Number(formData.id) || "");
  };
  const handleDeleteAllRows = () => {
    if (formData.id) {
      subtitleFilesApi.deleteAllRows(formData.id);
    }
  };
  const handleViewRows = (id: string): void => {
    navigate(`/timestamprow/${id}/list/`);
  };

  if (loadingData) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  const handleUploadClick = (formData: FormData) => {
    console.log("Button clicked:");
    fileEntityApi.uploadFile(formData, id ? id : "");

    // subtitleFilesApi.uploadFile(formData, Number(id));
  };
  const handleDeleteFileClick = () => {
    console.log("delete File");
    if (formData.file?.id !== undefined) {
      fileEntityApi.delete(formData.file.id);
    }
    formData.file = undefined;
    setFormData(formData);
    setLoading(false);
  };

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
        {id ? "Edit Subtitle File" : "Create Subtitle File"}
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
        <Box sx={{ mt: 3, display: "flex", justifyContent: "normal" }}>
         
         
          {formData.file ? (
            <>
              <Button variant="outlined" onClick={handleParseToRows}>
                Parse
              </Button>
              <Button variant="outlined" onClick={handleDeleteAllRows}>
                Dele all rows
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate("/subtitle-files")}
              >
                Cancel
              </Button>
            </>
          ) : (
            ""
          )}
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
      <IconButton onClick={() => formData.id && handleViewRows(formData.id.toString())} color="primary">
                      <PageviewIcon />
                    </IconButton>
      {formData.id && !formData.file ? (
        <ChoiceToUpload
          CustomText="choice file "
          onClick={handleUploadClick}
          onDeleteFile={handleDeleteFileClick}
        />
      ) : (
        <ChoiceToUpload
          CustomText={formData.file?.originalFilename || ""}
          onClick={handleUploadClick}
          onDeleteFile={handleDeleteFileClick}
        />
      )}

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

export default SubtitleFileForm;
