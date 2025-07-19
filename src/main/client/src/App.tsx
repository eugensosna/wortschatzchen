import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import { AppBar, Toolbar, Typography, Container, Button, Box } from '@mui/material';
import SubtitleFilesList from './components/SubtitleFiles/SubtitleFilesList';
import SubtitleFileForm from './components/SubtitleFiles/SubtitleFileForm';
 import LanguagesList from './components/Languages/LanguagesList';
 import LanguageForm from './components/Languages/LanguageForm';
 import WordsList from './components/words/WordsList';
import WordsForm from './components/words/WordsForm';
import { ExampleList2 } from './components/Example/ExampleList2';
import ExampleForm from './components/Example/ExampleForm';
import { TimestampRowList } from './components/TimestampRow/TimestampRow';
import TimestampRowForm from './components/TimestampRow/TimestampRowForm';
import { TimestampRowView } from './components/TimestampRow/TimestampRowView';
import TimestampRowGame from './components/TimestampRow/TimestampRowGame';

const App: React.FC = () => {
  return (
   
    <Router>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Subtitle Manager
          </Typography>
          <Button color="inherit" component={Link} to="/subtitle-files">
            Subtitle Files
          </Button>
          <Button color="inherit" component={Link} to="/languages">
            Languages
          </Button>
          <Button color="inherit" component={Link} to="/words">
            Words
          </Button>
        </Toolbar>
      </AppBar>
      <Container sx={{ mt: 4 }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/subtitle-files" element={<SubtitleFilesList />} />
          <Route path="/subtitle-files/new" element={<SubtitleFileForm />} />
          <Route path="/subtitle-files/edit/:id" element={<SubtitleFileForm />} />
          <Route path="/languages" element={<LanguagesList />} />
          <Route path="/languages/new" element={<LanguageForm />} />
          <Route path="/languages/edit/:id" element={<LanguageForm />} />
          <Route path="/timestamprow/:mainId/list/" element={<TimestampRowList />} />
          <Route path="/timestamprow/:mainId/game/" element={<TimestampRowGame />} />
          <Route path="/timestamprow/:mainId/game/:currentPage" element={<TimestampRowGame />} />
          <Route path="/timestamprow/:mainId/edit/:rowId" element={<TimestampRowForm />} />
          <Route path="/examples/:wordId/list/" element={<ExampleList2 />} />
          <Route path="/examples/:wordId/edit/:exampleId" element={<ExampleForm />} />
          <Route path="/words" element={<WordsList />} />
          <Route path="/words/new" element={<WordsForm />} />
          <Route path="/words/edit/:id" element={<WordsForm />} /> 
        </Routes>
      </Container>
    </Router>
  );
};

const Home: React.FC = () => {
  return (
    <Box sx={{ textAlign: 'center', mt: 10 }}>
      <Typography variant="h4" gutterBottom>
        Welcome to Subtitle Manager
      </Typography>
      <Typography variant="body1" paragraph>
        Manage your subtitle files and languages
      </Typography>
      <Box sx={{ mt: 4 }}>
        <Button 
          variant="contained" 
          component={Link} 
          to="/subtitle-files" 
          sx={{ mx: 2 }}
        >
          Manage Subtitle Files
        </Button>
        <Button 
          variant="contained" 
          component={Link} 
          to="/languages" 
          sx={{ mx: 2 }}
        >
          Manage Languages
        </Button>
        <Button 
          variant="contained" 
          component={Link} 
          to="/words" 
          sx={{ mx: 2 }}
        >
          Words
        </Button>
      </Box>
    </Box>
  );
};

export default App;