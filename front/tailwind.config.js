/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'mobalpa-green': '#1E4347',
        'mobalpa-green-alternate': '#316a70',
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
      height: {
        '0.1': '0.1rem',
        '84': '16rem',
        '86': '18rem',
        '88': '19rem',
        '90': '21rem',
        '90': '21rem',
        '95': '23rem',

        '100': '25rem', 
        '110': '26rem', 
        '114': '27rem', 
        '117': '29.5rem', 
        '116': '30rem', 
        '118': '32rem', 

        '120': '34.4rem',
        '121': '35.4rem',
        '122': '36.4rem',
        '123': '37.4rem',
        '124': '38.4rem', 
        'medium-carrousel': '188px',

      },
      width: {
        
          '100': '25rem',
          '110': '26rem',
          '114': '27rem',
          '117': '29.5rem',
          '116': '30rem',
          '118': '32rem',

        '120': '34.4rem',
        '121': '35.4rem',
        '122': '36.4rem',
        '123': '37.4rem',
        '124': '38.4rem',
        '125': '39.4rem',
        '126': '42.4rem',
        '127': '44.4rem',
        '128': '48.4rem', 

        'medium-carrousel': '285px',
        
      }
    },
  },
  plugins: [],
}
