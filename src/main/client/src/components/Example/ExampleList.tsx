// // src/components/Example/ExampleList.tsx
// import React, { useState } from "react";
// import { Button, ListItem, List, Modal } from "@mui/material";
// import { Example } from "../../types";
// import { useExamples } from "../contexts/ExampleContext";
// import ExampleForm from "./ExampleForm";
// //import ExampleForm from './ExampleForm';

// interface ExampleListProps {
//   wordId: number;
// }

// const ExampleList: React.FC<ExampleListProps> = ({ wordId }) => {
//   const { examples, loading, error, deleteExample, fetchExamples } =
//     useExamples();
//   const [showModal, setShowModal] = useState(false);
//   const [selectedExample, setSelectedExample] = useState<Example | null>(null);

//   React.useEffect(() => {
//     fetchExamples(wordId);
//   }, [wordId, fetchExamples]);

//   const handleEdit = (example: Example) => {
//     setSelectedExample(example);
//     setShowModal(true);
//   };

//   const handleDelete = (id: number) => {
//     if (window.confirm("Are you sure you want to delete this example?")) {
//       deleteExample(id);
//     }
//   };

//   const handleClose = () => {
//     setShowModal(false);
//     setSelectedExample(null);
//   };

//   if (loading) return <div>Loading examples...</div>;
//   if (error) return <div className="text-danger">{error}</div>;

//   return (
//     <div>
//       <Button
//         variant="contained"
//         onClick={() => setShowModal(true)}
//         className="mb-3"
//       >
//         Add New Example
//       </Button>

//       {examples.length === 0 ? (
//         <p>No examples found for this word.</p>
//       ) : (
//         <List dense={examples.length > 2 ? true : false}>
//           {examples.map((example) => (
//             <ListItem
//               key={example.id}
//               className="d-flex justify-content-between align-items-center"
//             >
//               <div>
//                 <strong>Order: {example.order}</strong>
//                 <p className="mb-0">{example.name}</p>
//               </div>
//               <div>
//                 <Button
//                   variant="contained"
//                   onClick={() => handleEdit(example)}
//                   className="me-2"
//                 >
//                   Edit
//                 </Button>
//                 <Button
//                   variant="outlined"
//                   onClick={() => handleDelete(example.id)}
//                 >
//                   Delete
//                 </Button>
//               </div>
//             </ListItem>
//           ))}
//         </List>
//       )}

//       {/* <Modal show={showModal} onHide={handleClose}>
//         <Modal.Header closeButton>
//           <Modal.Title>
//             {selectedExample ? "Edit Example" : "Add New Example"}
//           </Modal.Title>
//         </Modal.Header>
//         <Modal.Body>
//           <ExampleForm
//             wordId={wordId}
//             initialExample={selectedExample || undefined}
//             onClose={handleClose}
//           />
//         </Modal.Body>
//       </Modal> */}
//     </div>
//   );
// };

// export default ExampleList;
import React from 'react'

export default function ExampleList() {
  return (
    <div>ExampleList</div>
  )
}
