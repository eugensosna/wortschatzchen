
// src/components/common/FormDialog.tsx
import React, { ReactNode } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  IconButton,
  Box
} from '@mui/material';
import { Close as CloseIcon } from '@mui/icons-material';

interface FormDialogProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
  onSubmit: () => void;
  submitButtonText?: string;
  maxWidth?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  fullWidth?: boolean;
  disableSubmit?: boolean;
}

const FormDialog: React.FC<FormDialogProps> = ({
  open,
  onClose,
  title,
  children,
  onSubmit,
  submitButtonText = 'Save',
  maxWidth = 'sm',
  fullWidth = true,
  disableSubmit = false
}) => {
  return (
    <Dialog 
      open={open} 
      onClose={onClose} 
      maxWidth={maxWidth}
      fullWidth={fullWidth}
    >
      <DialogTitle>
        {title}
        <IconButton
          aria-label="close"
          onClick={onClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
          }}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent dividers>
        <Box component="form" noValidate sx={{ mt: 1 }}>
          {children}
        </Box>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="inherit">
          Cancel
        </Button>
        <Button onClick={onSubmit} color="primary" disabled={disableSubmit}>
          {submitButtonText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default FormDialog;
