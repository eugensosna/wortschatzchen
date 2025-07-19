import React from 'react';
import LanguageIcon from '@mui/icons-material/Language';
import { IconButton, Tooltip } from '@mui/material';

interface LanguageButtonProps {
  languageCode: string;
  onClick?: () => void;
}

const LanguageButton: React.FC<LanguageButtonProps> = ({ languageCode, onClick }) => {
  // Map language codes to full names
  const getLanguageName = (code: string): string => {
    const names: Record<string, string> = {
      'en': 'English',
      'de': 'German',
      'uk': 'Ukrainian',
      // Add more as needed
    };
    
    return names[code.toLowerCase()] || code;
  };

  return (
    <Tooltip title={getLanguageName(languageCode)}>
      <IconButton onClick={onClick}>
        <LanguageIcon />
        <span style={{ fontSize: '0.75rem', marginLeft: '2px' }}>{languageCode.toUpperCase()}</span>
      </IconButton>
    </Tooltip>
  );
};

export default LanguageButton;