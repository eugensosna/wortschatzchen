// src/components/SubtitleFiles/SubtitleFilesList.tsx
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Paper,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Snackbar,
  Alert,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  CircularProgress,
  Box,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import { languagesApi } from "../../api/languagesApi";
import { Language } from "../../types";

const LanguagesList: React.FC = () => {
  const [languages, setLanguages] = useState<Language[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success" as "success" | "error" | "info" | "warning",
  });
  const [deleteDialog, setDeleteDialog] = useState({
    open: false,
    fileId: null as string | null,
  });

  const navigate = useNavigate();

  useEffect(() => {
    loadLanguages();
  }, []);

  const loadLanguages = async (): Promise<void> => {
    try {
      setLoading(true);
      const data: Language[] = await languagesApi.getAll();
      setLanguages(data);
      setError(null);
    } catch (err) {
      setError("Failed to load languages");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddFile = (): void => {
    navigate("/languages/new");
  };

  const handleEditFile = (id: string): void => {
    navigate(`/languages/edit/${id}`);
  };

  const handleDeleteClick = (id: string): void => {
    setDeleteDialog({
      open: true,
      fileId: id,
    });
  };

  const confirmDelete = async (): Promise<void> => {
    if (!deleteDialog.fileId) return;

    try {
      await languagesApi.delete(Number(deleteDialog.fileId));
      setLanguages(
        languages.filter((item) => item.id !== Number(deleteDialog.fileId))
      );
      setSnackbar({
        open: true,
        message: "Language deleted successfully",
        severity: "success",
      });
    } catch (err) {
      setSnackbar({
        open: true,
        message: "Failed to delete language",
        severity: "error",
      });
      console.error(err);
    } finally {
      setDeleteDialog({ open: false, fileId: null });
    }
  };

  const closeSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mt: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Paper sx={{ p: 2 }}>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          mb: 2,
        }}
      >
        <Typography variant="h5">Languages</Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<AddIcon />}
          onClick={handleAddFile}
        >
          Add New
        </Button>
      </Box>

      {languages.length === 0 ? (
        <Typography variant="body1">
          No languages found. Click "Add New" to create one.
        </Typography>
      ) : (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Id</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>short Name</TableCell>

                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {languages.map((item) => (
                <TableRow key={item.id}>
                  <TableCell>{item.id}</TableCell>
                  <TableCell>{item.name}</TableCell>
                  <TableCell>{item.shortName}</TableCell>
                  <TableCell>
                    <IconButton
                      onClick={() => handleEditFile(item.id.toString())}
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
      )}

      <Dialog
        open={deleteDialog.open}
        onClose={() => setDeleteDialog({ ...deleteDialog, open: false })}
      >
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete this ? This action
            cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setDeleteDialog({ ...deleteDialog, open: false })}
          >
            Cancel
          </Button>
          <Button onClick={confirmDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>

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

export default LanguagesList;
