const URL = 'http://localhost:8081';
let entries = [];

let editEntryIndex = 0;

let createMode = 0;
let editMode = 1;

let mode = createMode;

const dateAndTimeToDate = (dateString, timeString) => {
    return new Date(`${dateString}T${timeString}`).toISOString();
};

const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    let entry = buildEntry(formData);
    switch (mode) {
        case createMode:
            createEntry(entry);
            break;
        case editMode:
            updateEntry(entry);
            break;
    }
}

const createEntry = (entry) => {
    fetch(`${URL}/entries`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(entry)
    }).then((result) => {
        result.json().then((entry) => {
            entries.push(entry);
            renderEntries();
        });
    });
};

const buildEntry = (formData) => {
    const entry = {};
    let checkInDate = formData.get('checkInDate');
    let checkOutDate = formData.get('checkOutDate');

    if (new Date(checkOutDate).getTime() < new Date(checkInDate).getTime()) {
        alert('Eingabedatum überschreitet Ausgabedatum.')
        return;
    }

    let checkInTime = formData.get('checkInTime');
    let checkOutTime = formData.get('checkOutTime');

    let checkInDateTime = dateAndTimeToDate(checkInDate, checkInTime);
    let checkOutDateTime = dateAndTimeToDate(checkOutDate, checkOutTime);

    if (Date.parse(checkOutDateTime) < Date.parse(checkInDateTime)) {
        alert('Eingabezeit überschreitet Ausgabezeit.');
        return;
    }

    entry['checkIn'] = checkInDateTime;
    entry['checkOut'] = checkOutDateTime;

    return entry;
}

const updateEntry = (entry) => {
    entry.id = entries[editEntryIndex].id;
    fetch(`${URL}/entries/${entry.id}`, {
        method: 'PUT', body: JSON.stringify(entry), headers: {
            'Content-Type': 'application/json'
        }
    })
        .then((response) => {
            if (response.status === 200) {
                entries = entries.map(value => {
                    if (value.id === entry.id) {
                        return entry;
                    }
                    return value;
                });
            }
        });
    mode = createMode;
}

const toggleEditEntry = (arrayIndex) => {
    editEntryIndex = arrayIndex;
    mode = editMode;
    let checkInDateTime = new Date(entries[arrayIndex].checkIn);
    let checkOutDateTime = new Date(entries[arrayIndex].checkOut);
    document.getElementById('checkInDate').value = checkInDateTime.toISOString().slice(0,10);
    document.getElementById('checkInTime').value = extractTime(checkInDateTime);
    document.getElementById('checkOutDate').value = checkOutDateTime.toISOString().slice(0,10);
    document.getElementById('checkOutTime').value = extractTime(checkOutDateTime);
}

const extractTime = (datetime) => {
    let hours = datetime.getHours();
    if (hours < 10) { hours = '0' + hours; }
    let minutes = datetime.getMinutes();
    if (minutes < 10) { minutes = '0' + minutes; }
    return `${hours}:${minutes}`;
}

const deleteEntry = (entryId) => {
    if (confirm('Bist du dir sicher?')) {
        fetch(`${URL}/entries/${entryId}`, {method: 'DELETE'})
            .then((response) => {
                if (response.status === 200) {
                    entries = entries.filter((el1) => el1.id !== entryId);
                    renderEntries();
                }
            });
    }
}

const indexEntries = () => {
    fetch(`${URL}/entries`, {
        method: 'GET'
    }).then((result) => {
        result.json().then((result) => {
            entries = result;
            renderEntries();
        });
    });
    renderEntries();
};

const createCell = (text) => {
    const cell = document.createElement('td');
    cell.innerText = text;
    return cell;
};

const renderEntries = () => {
    const display = document.querySelector('#entryDisplay');
    display.innerHTML = '';
    entries.forEach((entry, arrayIndex) => {
        const row = document.createElement('tr');
        row.appendChild(createCell(entry.id));
        row.appendChild(createCell(new Date(entry.checkIn).toLocaleString()));
        row.appendChild(createCell(new Date(entry.checkOut).toLocaleString()));
        const editButton = document.createElement('button');
        editButton.innerText = "Bearbeiten";
        editButton.onclick = () => toggleEditEntry(arrayIndex);
        row.appendChild(document.createElement('td').appendChild(editButton));
        const deleteButton = document.createElement('button');
        deleteButton.innerText = "Löschen";
        deleteButton.onclick = () => deleteEntry(entry.id);
        row.appendChild(document.createElement('td').appendChild(deleteButton));
        display.appendChild(row);
    });
};

document.addEventListener('DOMContentLoaded', function () {
    const createEntryForm = document.querySelector('#createEntryForm');
    createEntryForm.addEventListener('submit', handleSubmit);
    indexEntries();
});