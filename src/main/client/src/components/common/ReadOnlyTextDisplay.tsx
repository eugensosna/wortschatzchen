import React, { useRef } from 'react';
import { Box, Paper, Typography, IconButton, Tooltip } from '@mui/material';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';

interface ReadOnlyTextProps {
  text: string;
  label?: string;
}

const ReadOnlyTextDisplay: React.FC<ReadOnlyTextProps> = ({ text, label }) => {
  const textRef = useRef<HTMLDivElement>(null);

  const handleCopy = () => {
    if (textRef.current) {
      // Select the text
      const selection = window.getSelection();
      const range = document.createRange();
      range.selectNodeContents(textRef.current);
      selection?.removeAllRanges();
      selection?.addRange(range);
      
      // Copy the selected text
      navigator.clipboard.writeText(text)
        .then(() => {
          // Deselect after copying
          selection?.removeAllRanges();
        })
        .catch((err) => {
          console.error('Failed to copy text: ', err);
        });
    }
  };

  return (
    <Box sx={{ my: 2 }}>
      {label && (
        <Typography variant="subtitle1" sx={{ mb: 1 }}>
          {label}
        </Typography>
      )}
      <Paper
        variant="outlined"
        sx={{
          p: 2,
          display: 'flex',
          alignItems: 'flex-start',
          justifyContent: 'space-between',
          backgroundColor: '#f5f5f5',
          cursor: 'text',
        }}
      >
        <Typography
          ref={textRef}
          sx={{
            fontFamily: 'monospace',
            whiteSpace: 'pre-wrap',
            wordBreak: 'break-word',
            userSelect: 'all', // Makes text selectable
            flexGrow: 1,
          }}
          onClick={(e) => {
            // Select all text on click
            const selection = window.getSelection();
            const range = document.createRange();
            range.selectNodeContents(e.currentTarget);
            selection?.removeAllRanges();
            selection?.addRange(range);
          }}
        >
          {text}
        </Typography>
        <Tooltip title="Copy to clipboard">
          <IconButton 
            onClick={handleCopy} 
            size="small" 
            sx={{ ml: 1 }}
            aria-label="copy text"
          >
            <ContentCopyIcon fontSize="small" />
          </IconButton>
        </Tooltip>
      </Paper>
    </Box>
  );
};

export default ReadOnlyTextDisplay;