/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'mobalpa-green': '#1E4347',
        'emerald-green': '#639D87',
        'custom-red': '#772C21',
        'custom-blue': '#2C5B75',
        'custom-orange': '#D88951',
        'custom-green': '#464B2A',
      },
      fontFamily: {
        'gotham': ['Gotham Book', 'sans-serif'],
        'aquawax': ['Aquawax Medium', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
