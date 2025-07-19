package ua.sosna.wortschatz.wortschatzchen.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
	private final Path rootLocation;

	public FileSystemStorageService(StorageProperties properties) {
		super();
		 if(properties.getLocation().trim().length() == 0){
	            throw new StorageException("File upload location can not be Empty."); 
	        }

			this.rootLocation = Path.of(properties.getLocation());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {	
			throw new StorageException("Could not initialize storage", e);
		}
		
	}

	@Override
	public Path store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(
					Path.of(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
			return destinationFile;
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
		
	}
	
	public String getExtension(String fileName) {
	    char ch;
	    int len;
	    if(fileName==null || 
	            (len = fileName.length())==0 || 
	            (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
	             ch=='.' ) //in the case of . or ..
	        return "";
	    int dotInd = fileName.lastIndexOf('.'),
	        sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
	    if( dotInd<=sepInd )
	        return "";
	    else
	        return fileName.substring(dotInd+1).toLowerCase();
	}

}
