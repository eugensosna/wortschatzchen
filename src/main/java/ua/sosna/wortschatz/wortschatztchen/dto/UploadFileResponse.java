package ua.sosna.wortschatz.wortschatztchen.dto;

public class UploadFileResponse {

	  private String fileName;
	    private String fileDownloadUri;
	    private String contentType;
	    private Long fileId;
	    private long size;
	    private byte[] data;

	    public UploadFileResponse(String fileName, String contentType, Long fileId) {
	        this.fileName = fileName;
	        this.contentType = contentType;
	        this.fileId = fileId;
	    }
	    public UploadFileResponse(String fileName, String contentType, Long fileId, byte[] data) {
	        this.fileName = fileName;
	        this.contentType = contentType;
	        this.fileId = fileId;
	        this.data = data;
	    }
		/**
		 * @param fileName
		 * @param fileDownloadUri
		 * @param contentType
		 * @param fileId
		 * @param size
		 * @param data
		 */
		public UploadFileResponse(String fileName, String fileDownloadUri, String contentType, Long fileId, long size,
				byte[] data) {
			super();
			this.fileName = fileName;
			this.fileDownloadUri = fileDownloadUri;
			this.contentType = contentType;
			this.fileId = fileId;
			this.size = size;
			this.data = data;
		}
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFileDownloadUri() {
			return fileDownloadUri;
		}
		public void setFileDownloadUri(String fileDownloadUri) {
			this.fileDownloadUri = fileDownloadUri;
		}
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public Long getFileId() {
			return fileId;
		}
		public void setFileId(Long fileId) {
			this.fileId = fileId;
		}
		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
		public byte[] getData() {
			return data;
		}
		public void setData(byte[] data) {
			this.data = data;
		}

}
