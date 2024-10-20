// document.getElementById('rule-form').addEventListener('submit', function(event) {
//     event.preventDefault();

//     const ruleString = document.getElementById('ruleString').value;
//     let data;
//     try {
//         data = JSON.parse(document.getElementById('data').value);
//     } catch (e) {
//         alert("Invalid JSON format in Attributes.");
//         return;
//     }

//     fetch('/api/rules/evaluate?ruleString=' + encodeURIComponent(ruleString), {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//             'Authorization': 'Basic ' + btoa('admin:password') // Basic Auth credentials
//         },
//         body: JSON.stringify(data),
//     })
//     .then(response => response.json())
//     .then(result => {
//         console.log("Evaluation result received:", result);
//         const resultSpan = document.getElementById('result');
//         if (result) {
//             resultSpan.innerText = "Eligible";
//             resultSpan.style.color = "#388E3C"; // Green
//         } else {
//             resultSpan.innerText = "Not Eligible";
//             resultSpan.style.color = "#D32F2F"; // Red
//         }
//     })
//     .catch(error => {
//         console.error('Error:', error);
//         alert("An error occurred while evaluating the rule.");
//     });
// });

document.getElementById('rule-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const ruleString = document.getElementById('ruleString').value;
    let data;
    try {
        data = JSON.parse(document.getElementById('data').value);
    } catch (e) {
        alert("Invalid JSON format in Attributes.");
        return;
    }

    fetch('/api/rules/evaluate?ruleString=' + encodeURIComponent(ruleString), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa('admin:password') // Basic Auth credentials
        },
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(result => {
        console.log("Evaluation result received:", result);
        const resultSpan = document.getElementById('result');
        if (result) {
            resultSpan.innerText = "Eligible";
            resultSpan.style.color = "#388E3C"; // Green
        } else {
            resultSpan.innerText = "Not Eligible";
            resultSpan.style.color = "#D32F2F"; // Red
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("An error occurred while evaluating the rule.");
    });
});

// New event listener for creating a rule
document.getElementById('create-rule-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const newRuleString = document.getElementById('newRuleString').value;

    fetch('/api/rules/create?ruleString=' + encodeURIComponent(newRuleString), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Basic ' + btoa('admin:password') // Basic Auth credentials
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error creating rule");
        }
        return response.json();
    })
    .then(savedRule => {
        console.log("Rule created:", savedRule);
        const creationResultSpan = document.getElementById('creationResult');
        creationResultSpan.innerText = "Rule created successfully!";
        creationResultSpan.style.color = "#388E3C"; // Green
    })
    .catch(error => {
        console.error('Error:', error);
        const creationResultSpan = document.getElementById('creationResult');
        creationResultSpan.innerText = "Failed to create rule.";
        creationResultSpan.style.color = "#D32F2F"; // Red
    });
});
