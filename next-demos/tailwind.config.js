/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
    "./pages/**/*.{js,ts,jsx,tsx}",
    "./components/**/*.{js,ts,jsx,tsx}",
 
    // Or if using `src` directory:
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
    screens: {
      'tablet': '640px',

      'laptop': '1024px',

      'desktop': '1280px',
      '2xl': {'max': '1535px'},

      'xl': {'max': '1279px'},
      
      'pad': {'max': '1023px'},

      'lg': {'max': '1150px'},

      'md': {'max': '820px'},

      'sm': {'max': '639px'},
    },
  },
  plugins: [],
}