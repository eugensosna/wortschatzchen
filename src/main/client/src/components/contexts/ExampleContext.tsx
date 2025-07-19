// src/contexts/ExampleContext.tsx
import React, { createContext, useState, useContext, ReactNode } from "react";

import { Example, Word, ExampleCreateUpdate } from "../../types";
import { wordsApi } from "../../api/wordsApi";
import { examplesApi } from "../../api/examplesApi";

interface ExampleContextType {
  examples: Example[];
  loading: boolean;
  error: string | null;
  word: Word | null;
  fetchWord: (wordId: number) => Promise<void>;
  fetchExamples: (wordId: number) => Promise<void>;
  createExample: (example: Partial<Example>) => Promise<void>;
  updateExample: (id: number, data: Partial<Example>) => Promise<void>;
  deleteExample: (id: number) => Promise<void>;
}

const ExampleContext = createContext<ExampleContextType | undefined>(undefined);

export const ExampleProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [examples, setExamples] = useState<Example[]>([]);
  const [word, setWord] = useState<Word | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchExamples = async (wordId: number) => {
    setLoading(true);
    setError(null);
    try {
      const data = await examplesApi.getAllByWordId(wordId);
      setExamples(data);
      setLoading(false);
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const fetchWord = async (wordId: number) => {
    setLoading(true);
    setError(null);
    try {
      const data = await wordsApi.getById(wordId);
      setWord(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    }
  };

  const createExample = async (example: Partial<Example>) => {
    setLoading(true);
    setError(null);
    try {
      const exampleWithWord: ExampleCreateUpdate = {
        ...example,
        name: example.name ?? "",
		order: example.order ?? 0,
        word: word as Word, // Ensure word is included
      };
      const newExample = await examplesApi.create(exampleWithWord);
      setExamples((prev) => [...prev, newExample]);
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const updateExample = async (id: number, data: Partial<Example>) => {
    setLoading(true);
    setError(null);
    try {
      const existingExample = examples.find((example) => example.id === id);
      if (!existingExample) {
        throw new Error("Example not found");
      }
      const updatedExample = await examplesApi.update(id, {
        ...existingExample,
        ...data,
      });
      setExamples((prev) =>
        prev.map((example) => (example.id === id ? updatedExample : example))
      );
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const deleteExample = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      await examplesApi.delete(id);
      setExamples((prev) => prev.filter((example) => example.id !== id));
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  return (
    <ExampleContext.Provider
      value={{
        examples,
		word,
        loading,
        error,
		fetchWord,
        fetchExamples,
        createExample,
        updateExample,
        deleteExample,
      }}
    >
      {children}
    </ExampleContext.Provider>
  );
};

export const useExamples = () => {
  const context = useContext(ExampleContext);
  if (context === undefined) {
    throw new Error("useExamples must be used within an ExampleProvider");
  }
  return context;
};
