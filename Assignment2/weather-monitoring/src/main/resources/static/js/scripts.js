
const kelvinToCelsius = (kelvin) => {
    return kelvin;
};

// Function to convert Kelvin to Fahrenheit
const kelvinToFahrenheit = (kelvin) => {
    return (kelvin) * (9 / 5) + 32;
};

// Function to fetch weather data from the server
const fetchWeatherData = () => {
    fetch('/fetch-weather')
        .then(response => response.json())
        .then(data => {

            const tempUnit = document.querySelector('input[name="tempUnit"]:checked').value;

            // Convert temperatures based on the user preference
            let avgTemp, maxTemp, minTemp;

            if (tempUnit === 'C') {
                avgTemp = kelvinToCelsius(data.avgTemp).toFixed(2);
                maxTemp = kelvinToCelsius(data.maxTemp).toFixed(2);
                minTemp = kelvinToCelsius(data.minTemp).toFixed(2);
            } else {
                avgTemp = kelvinToFahrenheit(data.avgTemp).toFixed(2);
                maxTemp = kelvinToFahrenheit(data.maxTemp).toFixed(2);
                minTemp = kelvinToFahrenheit(data.minTemp).toFixed(2);
            }

            
            // Assuming your backend sends the weatherData and summary
            const weatherSummaryDiv = document.getElementById('weatherSummary');
            weatherSummaryDiv.innerHTML = `
                <h2>Application 2: Real-Time Data Processing System for Weather Monitoring with Rollups and Aggregates</h2>
                <p><strong>Objective:</strong> ${data.objective}</p>
                <p><strong>Data Source:</strong> ${data.dataSource}</p>
                <p><strong>Weather Data:</strong> ${data.weatherData}</p>
                <h3>Rollups and Aggregates:</h3>
                <ul>

                    <li><strong>Average Temperature:</strong> ${avgTemp} °${tempUnit}</li>
                    <li><strong>Maximum Temperature:</strong> ${maxTemp} °${tempUnit}</li>
                    <li><strong>Minimum Temperature:</strong> ${minTemp} °${tempUnit}</li>
                    <li><strong>Dominant Weather Condition:</strong> ${data.dominantWeather}</li>
                </ul>
            `;
        })
        .catch(error => console.error('Error fetching weather data:', error));
};

// Fetch weather data immediately on page load
fetchWeatherData();

// Set up periodic fetching every 5 minutes (300,000 milliseconds)
const intervalId = setInterval(fetchWeatherData, 300000);

// Event listener for the button to fetch data manually
document.getElementById('fetchWeather').addEventListener('click', fetchWeatherData);

// Sample function to fetch and visualize historical temperature data
const fetchHistoricalData = () => {
    fetch('/fetch-historical-weather')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(entry => entry.date); // Assuming each entry has a date
            const avgTemps = data.map(entry => entry.avgTemp); // Assuming avgTemp is stored

            const ctx = document.getElementById('temperatureChart').getContext('2d');
            const myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Average Temperature',
                        data: avgTemps,
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1,
                        fill: false
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching historical weather data:', error));
};

// Call the function to fetch and visualize data
fetchHistoricalData();


