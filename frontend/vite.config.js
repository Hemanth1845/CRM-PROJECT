import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // This 'base' property is ESSENTIAL for the Tomcat deployment to work.
  // base: '/crm-frontend/',
  server:{
    port:4000,
  }
})

