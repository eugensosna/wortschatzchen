// src/components/common/DataTable.tsx
import React, { useState } from 'react';
import { 
  Paper, Table, TableBody, TableCell, TableContainer, TableHead, 
  TablePagination, TableRow, IconButton, Tooltip, Box,
  LinearProgress, Typography
} from '@mui/material';
import { 
  Edit as EditIcon, 
  Delete as DeleteIcon,
  Visibility as ViewIcon
} from '@mui/icons-material';

interface Column {
  id: string;
  label: string;
  minWidth?: number;
  align?: 'right' | 'left' | 'center';
  format?: (value: unknown) => string;
}

interface DataTableProps {
  columns: Column[];
  data: unknown[];
  isLoading: boolean;
  onView?: (row: unknown) => void;
  onEdit?: (row: unknown) => void;
  onDelete?: (row: unknown) => void;
  title?: string;
}

const DataTable: React.FC<DataTableProps> = ({
  columns,
  data,
  isLoading,
  onView,
  onEdit,
  onDelete,
  title
}) => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      {title && (
        <Box sx={{ p: 2, borderBottom: '1px solid rgba(224, 224, 224, 1)' }}>
          <Typography variant="h6" component="div">
            {title}
          </Typography>
        </Box>
      )}
      
      {isLoading && <LinearProgress />}
      
      <TableContainer sx={{ maxHeight: 440 }}>
        <Table stickyHeader aria-label="sticky table">
          <TableHead>
            <TableRow>
              {columns.map((column) => (
                <TableCell
                  key={column.id}
                  align={column.align || 'left'}
                  style={{ minWidth: column.minWidth || 100 }}
                >
                  {column.label}
                </TableCell>
              ))}
              <TableCell align="right" style={{ minWidth: 120 }}>
                Actions
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.length > 0 ? (
              data
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((row, index) => {
                  return (
                    <TableRow hover role="checkbox" tabIndex={-1} key={index}>
                      {columns.map((column) => {
                        const value = row[column.id];
                        return (
                          <TableCell key={column.id} align={column.align || 'left'}>
                            {column.format ? column.format(value) : value}
                          </TableCell>
                        );
                      })}
                      <TableCell align="right">
                        {onView && (
                          <Tooltip title="View">
                            <IconButton onClick={() => onView(row)} size="small">
                              <ViewIcon />
                            </IconButton>
                          </Tooltip>
                        )}
                        {onEdit && (
                          <Tooltip title="Edit">
                            <IconButton onClick={() => onEdit(row)} size="small">
                              <EditIcon />
                            </IconButton>
                          </Tooltip>
                        )}
                        {onDelete && (
                          <Tooltip title="Delete">
                            <IconButton onClick={() => onDelete(row)} size="small" color="error">
                              <DeleteIcon />
                            </IconButton>
                          </Tooltip>
                        )}
                      </TableCell>
                    </TableRow>
                  );
                })
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length + 1} align="center">
                  {isLoading ? 'Loading data...' : 'No data available'}
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[10, 25, 100]}
        component="div"
        count={data.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </Paper>
  );
};

export default DataTable;
