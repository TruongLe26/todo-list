document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('createGroupTodoItemModal');
    const form = document.getElementById('createGroupTodoItemForm');

    modal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const groupId = button.getAttribute('data-group-id');
        form.setAttribute("action", `/groups/saveGroupTodoItem/${groupId}`);
        document.getElementById('groupIdHidden').value = groupId;
    });

    document.getElementById('createGroupTodoItemForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const groupId = document.getElementById('groupIdHidden').value;
        const formData = new URLSearchParams();
        formData.append('title', document.getElementById('createGroupTodoItemTitle').value);
        formData.append('description', document.getElementById('createGroupTodoItemDescription').value);
        formData.append('complete', document.getElementById('createGroupTodoItemComplete').checked);

        try {
            const response = await fetch(`/groups/saveGroupTodoItem/${groupId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content,
                },
                body: formData
            });

            if (response.ok) {
                bootstrap.Modal.getInstance(modal).hide();
                location.reload();
            } else {
                alert('Failed to create item.');
            }
        } catch (error) {
            console.error('Error during creation:', error);
        }
    });
});