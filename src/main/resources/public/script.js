const URL = 'http://localhost:8081';
let entries = [];

const dateAndTimeToDate = (dateString, timeString) => {
    return new Date(`${dateString}T${timeString}`).toISOString();
};

const createEntry = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const entry = {};
    let checkInDate = formData.get('checkInDate');
    let checkOutDate = formData.get('checkOutDate');

    if (new Date(checkOutDate).getTime() < new Date(checkInDate).getTime()) {
        alert('Input Date exceeds Output Date.')
        return;
    }

    let checkInTime = formData.get('checkInTime');
    let checkOutTime = formData.get('checkOutTime');

    if (new Date(`0000-01-01 ${checkOutTime}`).getTime() < new Date(`0000-01-01 ${checkInTime}`)) {
        alert('Input Time exceeds Output Time.')
        return;
    }

    entry['checkIn'] = dateAndTimeToDate(checkInDate, checkInTime);
    entry['checkOut'] = dateAndTimeToDate(checkOutDate, checkOutTime);

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
    entries.forEach((entry) => {
        const row = document.createElement('tr');
        row.appendChild(createCell(entry.id));
        row.appendChild(createCell(new Date(entry.checkIn).toLocaleString()));
        row.appendChild(createCell(new Date(entry.checkOut).toLocaleString()));
        const deleteButton = document.createElement('button');
        deleteButton.innerText = "Löschen";
        deleteButton.onclick = () => deleteEntry(entry.id);
        row.appendChild(document.createElement('td').appendChild(deleteButton));
        display.appendChild(row);
    });
};

document.addEventListener('DOMContentLoaded', function(){
    const createEntryForm = document.querySelector('#createEntryForm');
    createEntryForm.addEventListener('submit', createEntry);
    indexEntries();
});