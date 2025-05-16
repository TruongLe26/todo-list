document.addEventListener('DOMContentLoaded', () => {
    const updateModal = document.getElementById('updateGroupTodoItemModal');
    const updateForm = document.getElementById('updateGroupTodoItemForm');

    updateModal.addEventListener('show.bs.modal', (event) => {
        const button = event.relatedTarget;
        const id = button.getAttribute('data-id');
        const title = button.getAttribute('data-title');
        const description = button.getAttribute('data-description');
        const complete = button.getAttribute('data-complete') === 'true';

        document.getElementById('groupTodoId').value = id;
        document.getElementById('groupTodoItemTitle').value = title;
        document.getElementById('groupTodoItemDescription').value = description;
        document.getElementById('groupTodoItemComplete').checked = complete;
        document.getElementById('groupTodoItemGroupId').value = button.getAttribute('data-group-id');
    });

    updateForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = document.getElementById('groupTodoId').value;
        const title = document.getElementById('groupTodoItemTitle').value;
        const description = document.getElementById('groupTodoItemDescription').value;
        const complete = document.getElementById('groupTodoItemComplete').checked;
        const groupId = document.getElementById('groupTodoItemGroupId').value;

        const formData = new URLSearchParams();
        formData.append('id', id);
        formData.append('title', title);
        formData.append('description', description);
        formData.append('complete', complete);

        const csrfToken = document.querySelector('meta[name="_csrf"]').content;

        try {
            const response = await fetch(`/groups/updateGroupTodoItem/${groupId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: formData
            });

            if (response.ok) {
                const modalInstance = bootstrap.Modal.getInstance(updateModal);
                modalInstance.hide();
                location.reload();
            } else {
                alert('Failed to update todo item');
            }
        } catch (error) {
            console.error('Error updating todo item:', error);
            alert('An error occurred. See console.');
        }
    });
});