import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [word, setWord] = useState({
    name: 'machen', 
  });

  
  

  return (
    <>
     <h1>Hello World {word.name} </h1>
     <h2>this is my first React</h2>
    </>
  )
}

export default App
