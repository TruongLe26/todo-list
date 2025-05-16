document.getElementById('createForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const formData = new URLSearchParams();
    formData.append('title', document.getElementById('createTitle').value);
    formData.append('description', document.getElementById('createDescription').value);
    formData.append('complete', document.getElementById('createComplete').checked);

    try {
        const response = await fetch('/saveTodoItem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content,
            },
            body: formData
        });

        if (response.ok) {
            const modalEl = document.querySelector('#createModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();
            location.reload();
        } else {
            alert('Failed to create item.');
        }
    } catch (error) {
        console.error('Error during creation:', error);
    }
});