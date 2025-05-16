document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".btn-update").forEach(button => {
        button.addEventListener("click", function () {
            const id = this.getAttribute("data-id");
            const title = this.getAttribute("data-title");
            const description = this.getAttribute("data-description");
            const complete = this.getAttribute("data-complete") === "true";

            document.getElementById("todoId").value = id;
            document.getElementById("title").value = title;
            document.getElementById("description").value = description;
            document.getElementById("complete").checked = complete;

            document.getElementById("updateForm").action = "/updateTodoItem";
        });
    });
});

document.getElementById('updateForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const formData = new URLSearchParams();
    formData.append('id', document.getElementById('todoId').value);
    formData.append('title', document.getElementById('title').value);
    formData.append('description', document.getElementById('description').value);
    formData.append('complete', document.getElementById('complete').checked);

    try {
        const response = await fetch('/updateTodoItem', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
            },
            body: formData
        });

        if (response.ok) {
            const modalEl = document.querySelector('#updateModal');
            const modal = bootstrap.Modal.getInstance(modalEl);
            modal.hide();
            location.reload();
        } else {
            alert('Failed to update item.');
        }
    } catch (error) {
        console.error('Error during update:', error);
    }
});