import { IconButton } from '@mui/material';
import DeleteIcon from "@mui/icons-material/Delete";

import React from 'react'

type ChoiceToUploadProps = {
	onClick: ( data:FormData) => void;
    onDeleteFile: ()=> void;
    CustomText?: string;
}

const ChoiceToUpload: React.FC<ChoiceToUploadProps> = ({onClick, onDeleteFile, CustomText}) => {

	const [selectedFile, setSelectedFile] = React.useState<File | null>(null);
	// On file select (from the pop up)
    const onFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        // Update the state
        if (event.target.files) {
            setSelectedFile(event.target.files[0]);
        }
    };

	// On file upload (click the upload button)
    const onFileUpload = () => {
        // Create an object of formData
        const postFormData = new FormData();
		console.log(selectedFile);

        // Update the formData object
        if (selectedFile) {
            postFormData.append(
                "file",
                selectedFile,
                selectedFile.name
            );
			onClick(postFormData);

			setSelectedFile(null);

        }

        // Details of the uploaded file
        console.log(selectedFile);

        // Request made to the backend api
        // Send formData object
        //axios.post("api/uploadfile", formData);
    };

    const handleDeleteClick = (): void => {

        onDeleteFile();
      };



 	// File content to be displayed after
    // file upload is complete
    const FileData = () => {

        if (selectedFile) {
            return (
                <div>
                    <h2>File Details:</h2>
                    
                    <p>File Name: {selectedFile.name}</p>

                    <p>File Type: {selectedFile.type}</p>

                    <p>
                        Last Modified:
                        {selectedFile?.lastModified.toLocaleString()}
                    </p>
                </div>
            );
        } else {
            return (
                <div>
                    <br />
                    <h4>Choose before Pressing the Upload button</h4>
                </div>
            );
        }
    };


	return (
		<div>
            <div className='container'>
                
            <p>{CustomText}</p>
            <IconButton
                      onClick={() => handleDeleteClick()}
                      color="error"
                    >
                      <DeleteIcon />
                    </IconButton>
                    </div>
		<h3>File Upload </h3>
		<div>
			<input type="file" onChange={onFileChange} />
			<button onClick={onFileUpload}>Upload!</button>
		</div>
		<FileData />
	</div>
	);
}

export default ChoiceToUpload
