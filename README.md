# Zeotap
 
# Rule Engine with AST

## Overview
This project implements a simple 3-tier rule engine application designed to determine user eligibility based on various attributes such as age, department, salary, and experience. The application uses an Abstract Syntax Tree (AST) to represent conditional rules and allows for dynamic creation, combination, and modification of these rules.

## Features
- **Rule Creation**: Users can create individual rules in a simple format.
- **Rule Combination**: Multiple rules can be combined into a single AST.
- **Rule Evaluation**: The engine evaluates rules against user-provided data to determine eligibility.
- **Dynamic AST Representation**: Rules are represented as ASTs, enabling efficient evaluations.
- **Error Handling**: The system handles invalid rule strings and formats gracefully.

## Data Structure
The primary data structure used is `Node`, which represents the AST:
- **type**: Indicates the node type (`"operator"` for AND/OR, `"operand"` for conditions).
- **left**: Reference to the left child node.
- **right**: Reference to the right child node (for operators).
- **value**: Optional value for operand nodes (e.g., number for comparisons).

## Sample Rules
- `rule1`: `((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing'))`
- `rule2`: `((age >= 18 AND age <= 25) AND department = 'Internship') OR ((age > 25 AND department = 'Full-Time') AND (salary >= 30000 AND experience > 2))`

## API Endpoints
- **POST /api/rules/create**: Create a new rule from a string.
- **POST /api/rules/combine**: Combine multiple rules into a single AST.
- **POST /api/rules/evaluate**: Evaluate a rule against provided data attributes.
- **GET /api/rules/get/{id}**: Retrieve a rule by ID.
- **PUT /api/rules/modify/{id}**: Modify an existing rule.

## Frontend
The frontend consists of a simple HTML form that allows users to input rules and attributes in JSON format:
- **Rule Input**: Users can enter rules like `age >= 18 AND department = 'Internship'`.
- **Attributes Input**: Users provide attributes in JSON format (e.g., `{"age": 22, "department": "Internship", "salary": 25000, "experience": 1}`).

## Running the Application
1. Clone the repository.
2. Navigate to the project directory.
3. Set up the database (MySQL) and configure application properties.
4. Build and run the Spring Boot application.
5. Open the frontend in a web browser.

## Testing
Use the following test cases to validate the rule engine functionality:
1. **Create Rule**: `age >= 18 AND department = 'Internship'`
2. **Evaluate Rule**: Test with various attributes to check eligibility.
3. **Combine Rules**: Combine rules to see if the AST reflects the combined logic.

## Contributing
Feel free to contribute to this project by opening issues or submitting pull requests.

## License
This project is licensed under the MIT License - see the LICENSE file for details.


# Real-Time Weather Monitoring System

## Overview
This project is a real-time data processing system designed to monitor weather conditions in Indian metropolitan areas. It retrieves weather data from the OpenWeatherMap API, performs rollups and aggregates, triggers alerts based on user-defined thresholds, and provides visualizations for daily summaries and trends.

## Features
- Real-time weather data retrieval
- Temperature conversion (Kelvin to Celsius and Fahrenheit)
- Daily summary rollups and aggregates
- Alerting mechanisms for weather condition thresholds
- Historical weather data visualization
- User-friendly interface for displaying weather summaries

## Technologies Used
- **Backend**: Spring Boot
- **Frontend**: HTML, CSS, JavaScript
- **Database**: MySQL
- **APIs**: OpenWeatherMap API
- **Charting**: Chart.js for visualizations

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/weather-monitoring.git
