document.getElementById('createGroupForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const formData = new URLSearchParams();
    formData.append('name', document.getElementById('createGroupName').value);

    try {
        const response = await fetch('/groups/createGroup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content,
            },
            body: formData
        });

        if (response.ok) {
            const modalEl = document.querySelector('#createGroupModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();
            location.reload();
        } else {
            alert('Failed to create group.');
        }
    } catch (error) {
        console.error('Error during group creation:', error);
    }
});