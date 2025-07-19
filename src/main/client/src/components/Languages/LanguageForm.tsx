
// src/components/SubtitleFiles/SubtitleFileForm.tsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  CircularProgress,
  Alert,
  Snackbar} from '@mui/material';

import { languagesApi } from '../../api/languagesApi';
import {  Language } from '../../types';
import LanguageIcon from '../common/LanguageIcon';

const LanguageForm: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState<Partial<Language>>({
	name:'',
	shortName: ''
  });
  
  const [loading, setLoading] = useState<boolean>(false);
  const [loadingData, setLoadingData] = useState<boolean>(id ? true : false);
  const [error, setError] = useState<string | null>(null);
  const [snackbar, setSnackbar] = useState({
	open: false,
	message: '',
	severity: 'success' as 'success' | 'error' | 'info' | 'warning'
  });

  useEffect(() => {
	
	const loadLanguage = async () => {
	  if (!id) return;
	  
	  try {
		setLoadingData(true);
		const data = await languagesApi.getById(Number(id));
		setFormData(data);
	  } catch (err) {
		setError('Failed to load subtitle file');
		console.error(err);
	  } finally {
		setLoadingData(false);
	  }
	};

	loadLanguage();
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
	const { name, value } = e.target;
	setFormData({ ...formData, [name]:  value });
  };

//   const handleLanguageChange = (e: SelectChangeEvent) => {
// 	const langId = Number(e.target.value);
// 	const selectedLang = languages.find(lang => lang.id === langId);
	
// 	setFormData({
// 	  ...formData,
// 	  baseLang: selectedLang
// 	});
// 	//setFormData({ ...formData, languageId: e.target.value });
//   };

  const handleSubmit = async (e: React.FormEvent) => {
	e.preventDefault();
	
	try {
	  setLoading(true);
	  
	  if (id) {
		await languagesApi.update(Number(id), formData as Language);
		// await updateLanguage(id, formData);
		setSnackbar({
		  open: true,
		  message: 'Language file updated successfully',
		  severity: 'success'
		});
	  } else {
		await languagesApi.create(formData as Omit<Language, 'id' | 'createdAt' | 'updatedAt'>);
		// await createLanguage(formData as Omit<Language, 'id' | 'createdAt' | 'updatedAt'>);
		setSnackbar({
		  open: true,
		  message: 'Language file created successfully',
		  severity: 'success'
		});
	  }
	  
	  // Navigate back after short delay to show the success message
	  setTimeout(() => navigate('/languages'), 1500);
	} catch (err) {
	  setError('Failed to save ');
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
	  <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
		<CircularProgress />
	  </Box>
	);
  }

  return (
	<Paper sx={{ p: 3 }}>
	  <Typography variant="h5" gutterBottom>
		{id ? 'Edit ' : 'Create '}
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
		  value={formData.name || ''}
		  onChange={handleChange}
		  fullWidth
		  margin="normal"
		  required
		/>
		<TextField
		  label="shortName"
		  name="shortName"
		  value={formData.shortName || ''}
		  onChange={handleChange}
		  fullWidth
		  margin="normal"
		  required
		/>
		<Box sx={{m:2, display:'flex', justifyContent: 'start'}}>
		<LanguageIcon languageCode={formData.flagId || ''} />
		<TextField
		  label="flagId"
		  name="flagId"
		  value={formData.flagId || ''}
		  onChange={handleChange}
		  fullWidth
		  margin="normal"
		  required
		/>




		</Box>
		<Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
		  <Button 
			variant="outlined" 
			onClick={() => navigate('/subtitle-files')}
		  >
			Cancel
		  </Button>
		  
		  <Button 
			type="submit" 
			variant="contained" 
			color="primary"
			disabled={loading}
		  >
			{loading ? <CircularProgress size={24} /> : id ? 'Update' : 'Create'}
		  </Button>
		</Box>
	  </form>
	  
	  <Snackbar open={snackbar.open} autoHideDuration={6000} onClose={closeSnackbar}>
		<Alert onClose={closeSnackbar} severity={snackbar.severity}>
		  {snackbar.message}
		</Alert>
	  </Snackbar>
	</Paper>
  );
};

export default LanguageForm;