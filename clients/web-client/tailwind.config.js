/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {},
  },
  daisyui: {
    themes: [
      {
        "fxrepo": {
          'primary': '#ff0000', // Red
          'primary-focus': '#cc0000', // Darker Red
          'primary-content': '#ffffff', // White

          'secondary': '#ff4500', // OrangeRed
          'secondary-focus': '#cc3700', // Darker OrangeRed
          'secondary-content': '#ffffff', // White

          'accent': '#ffff00', // Yellow
          'accent-focus': '#cccc00', // Darker Yellow
          'accent-content': '#000000', // Black

          'neutral': '#808080', // Gray
          'neutral-focus': '#666666', // Darker Gray
          'neutral-content': '#ffffff', // White

          'base-100': '#000000', // Black
          'base-200': '#121212', // Slightly lighter Black
          'base-300': '#242424', // Even lighter Black
          'base-content': '#ffffff', // White

          'info': '#008080', // Teal
          'info-content': '#ffffff', // White

          'success': '#008000', // Green
          'success-content': '#ffffff', // White

          'warning': '#ffa500', // Orange
          'warning-content': '#000000', // Black

          'error': '#800000', // Maroon
          'error-content': '#ffffff', // White
        }
      }
    ]
  },
  plugins: [
    require('daisyui'),
  ],
}

