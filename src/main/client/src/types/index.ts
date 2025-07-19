// src/types/index.ts - Type definitions

// Common interfaces
export interface BaseEntity {
  id: number;
  uuid?: string;
  createdAt?: string;
  updatedAt?: string;
}
export interface File extends BaseEntity {
  name: string;
  extension: string;
  size: number;
  mimeType: string;
  contentType: string;
  originalFilename: string;
  fileName: string;
}

export interface Word extends BaseEntity {
  name: string;
  baseLang?: Language;
  mean?: string;
}

export interface WordCreateUpdate {
  name: string;
  languageId: number;
  // Add other properties as needed
}

// SubtitleFile entities
export interface SubtitleFile extends BaseEntity {
  // Add subtitle file properties
  name: string;
  uploadDate: string;
  baseLang?: Language;
  file?: File;
}

export interface SubtitleFileCreateUpdate {
  name: string;

  // Add other properties as needed
}

// Language entities
export interface Language extends BaseEntity {
  name: string;
  shortName: string;
  flagId?:string;
}

export interface LanguageCreateUpdate {
  name: string;
  shortName: string;
}

export interface Example extends BaseEntity {
  name: string;
  order: number;
  baseWordId: number;
}

export interface ExampleCreateUpdate {
  name: string;
  order: number;
  baseWordId: number;
}

export interface Mean extends BaseEntity {
  name: string;
  order: number;
  baseWord: Word;
  baseLang: Language;
}

export interface TranslatedWord extends BaseEntity {
  uuidClass: string;
  name: string;
  baseLang: Language;
  translatedName: string;
  targetLang: Language;
}
export interface TranslatedWordToTranslate {
  from: string;
  to: string;
  text: string;
  

}

export interface TimestampRow extends BaseEntity {
  text: string;
  subtitleFile?: SubtitleFile;
}
export interface TimestampRowPageable {
  content: TimestampRow[];
  totalElements:number;
}



