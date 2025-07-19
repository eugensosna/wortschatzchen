// src/components/SubtitleFiles/SubtitleFilesList.tsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
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
  Box
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import PageviewIcon from '@mui/icons-material/Pageview';
import { subtitleFilesApi } from '../../api/subtitleFilesApi';
import { SubtitleFile } from '../../types';
import CasinoIcon from '@mui/icons-material/Casino';


const SubtitleFilesList: React.FC = () => {
  const [subtitleFiles, setSubtitleFiles] = useState<SubtitleFile[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success' as 'success' | 'error' | 'info' | 'warning'
  });
  const [deleteDialog, setDeleteDialog] = useState({
    open: false,
    fileId: null as string | null
  });
  
  const navigate = useNavigate();

  useEffect(() => {
    loadSubtitleFiles();
  }, []);

  const loadSubtitleFiles = async (): Promise<void> => {
    try {
      setLoading(true);
      const data: SubtitleFile[] = await subtitleFilesApi.getAll();
      setSubtitleFiles(data);
      setError(null);
    } catch (err) {
      setError('Failed to load subtitle files');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddFile = (): void => {
    navigate('/subtitle-files/new');
  };

  const handleEditFile = (id: string): void => {
    navigate(`/subtitle-files/edit/${id}`);
  };

  const handleViewRows = (id: string): void => {
    navigate(`/timestamprow/${id}/list/`);
  };


  const handleViewGame = (id: string): void => {
    navigate(`/timestamprow/${id}/game/1`);
  };

  const handleDeleteClick = (id: string): void => {
    setDeleteDialog({
      open: true,
      fileId: id
    });
  };

  const confirmDelete = async (): Promise<void> => {
    if (!deleteDialog.fileId) return;
    
    try {
      await subtitleFilesApi.delete(Number(deleteDialog.fileId));
      //await deleteSubtitleFile(deleteDialog.fileId);
      setSubtitleFiles(subtitleFiles.filter(file => file.id !== Number(deleteDialog.fileId)));
      setSnackbar({
        open: true,
        message: 'Subtitle file deleted successfully',
        severity: 'success'
      });
    } catch (err) {
      setSnackbar({
        open: true,
        message: 'Failed to delete subtitle file',
        severity: 'error'
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
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
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
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h5">Subtitle Files</Typography>
        <Button 
          variant="contained" 
          color="primary" 
          startIcon={<AddIcon />}
          onClick={handleAddFile}
        >
          Add New
        </Button>
      </Box>
      
      {subtitleFiles.length === 0 ? (
        <Typography variant="body1">No subtitle files found. Click "Add New" to create one.</Typography>
      ) : (
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Id</TableCell>
                <TableCell>Name</TableCell>
                <TableCell>Language</TableCell>
                <TableCell> file </TableCell>
                
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {subtitleFiles.map((file) => (
                <TableRow key={file.id}>
                  <TableCell>{file.id}</TableCell>
                  <TableCell>{file.name}</TableCell>
                  <TableCell>{file.baseLang?.shortName}</TableCell>
                  <TableCell> {file.file?.fileName}</TableCell>
                  <TableCell>
                  <IconButton onClick={() => handleViewGame(file.id.toString())} color="primary">
                      <CasinoIcon  />
                    </IconButton>

                  <IconButton onClick={() => handleViewRows(file.id.toString())} color="primary">
                      <PageviewIcon />
                    </IconButton>

                    <IconButton onClick={() => handleEditFile(file.id.toString())} color="primary">
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDeleteClick(file.id.toString())} color="error">
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      <Dialog open={deleteDialog.open} onClose={() => setDeleteDialog({ ...deleteDialog, open: false })}>
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete this subtitle file? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialog({ ...deleteDialog, open: false })}>Cancel</Button>
          <Button onClick={confirmDelete} color="error">Delete</Button>
        </DialogActions>
      </Dialog>

      <Snackbar open={snackbar.open} autoHideDuration={6000} onClose={closeSnackbar}>
        <Alert onClose={closeSnackbar} severity={snackbar.severity}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Paper>
  );
};

export default SubtitleFilesList;
