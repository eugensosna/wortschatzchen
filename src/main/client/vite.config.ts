import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { OpenInBrowser } from '@mui/icons-material'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server :{
  port: 3000,
  host:"0.0.0.0", 
  allowedHosts: true,
  
    proxy: {
      '/api': {
        target: 'http://dellprecl:8080',
        changeOrigin: true,
      }
    },
    open: true 
}
})
