document.addEventListener('DOMContentLoaded', () => {
    let selectionMode = false;
    const toggleBtn = document.getElementById('selectToggle');
    const toggleIcon = toggleBtn.querySelector('i');
    const selectedIdsContainer = document.getElementById('selectedIdsContainer');
    const batchUpdateBtn = document.getElementById('batchUpdateBtn');
    const batchDeleteBtn = document.getElementById('batchDeleteBtn');

    toggleBtn.addEventListener('click', () => {
        selectionMode = !selectionMode;

        if (selectionMode) {
            toggleIcon.textContent = 'check_box_outline_blank';
        } else {
            toggleIcon.textContent = 'check_box';
        }

        if (!selectionMode) {
            document.querySelectorAll('.table-row-selected').forEach(row => {
                row.classList.remove('table-row-selected');
            });
            selectedIdsContainer.innerHTML = '';
            batchUpdateBtn.disabled = true;
            batchDeleteBtn.disabled = true;
        }
    });

    document.querySelectorAll('tbody tr').forEach(row => {
        const id = row.dataset.id;

        row.querySelectorAll('.btn-update, .btn-delete').forEach(btn => {
            btn.addEventListener('click', e => e.stopPropagation());
        });

        row.addEventListener('click', () => {
            if (!selectionMode) return;

            row.classList.toggle('table-row-selected');

            const inputId = `selected-${id}`;
            const existingInput = document.getElementById(inputId);

            if (row.classList.contains('table-row-selected')) {
                if (!existingInput) {
                    const input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'selectedIds';
                    input.value = id;
                    input.id = inputId;
                    selectedIdsContainer.appendChild(input);
                }
            } else {
                if (existingInput) existingInput.remove();
            }

            const hasSelected = selectedIdsContainer.querySelectorAll('input').length > 0;
            batchUpdateBtn.disabled = !hasSelected;
            batchDeleteBtn.disabled = !hasSelected;
        });
    })

    batchDeleteBtn.addEventListener('click', async () => {
        const selectedInputs = selectedIdsContainer.querySelectorAll('input[name="selectedIds"]');
        const selectedIds = Array.from(selectedInputs).map(input => input.value);

        if (selectedIds.length === 0) {
            alert("No items selected.");
            return;
        }

        try {
            const params = new URLSearchParams();
            selectedIds.forEach(id => params.append('selectedIds', id));

            const response = await fetch('/deleteTodoItems', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                },
                body: params
            });

            if (response.ok) {
                window.location.href = '/';
            } else {
                alert("Status: " + response.status);
            }
        } catch (error) {
            alert('Error during batch delete.');
        }
    })
});