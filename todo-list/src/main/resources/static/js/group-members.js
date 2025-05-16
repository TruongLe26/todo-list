document.addEventListener("DOMContentLoaded", function () {
    const addMemberModal = document.getElementById("addMemberModal");

    addMemberModal.addEventListener("show.bs.modal", function (event) {
        const triggerBtn = event.relatedTarget;
        document.getElementById("groupIdForAdd").value = triggerBtn.getAttribute("data-group-id");
        document.getElementById("memberEmail").value = "";
        document.getElementById("addMemberError").style.display = "none";
    });

    document.getElementById("addMemberForm").addEventListener("submit", async function (event) {
        event.preventDefault();

        const groupId = document.getElementById("groupIdForAdd").value;
        const email = document.getElementById("memberEmail").value;

        const formData = new URLSearchParams();
        formData.append("email", email);

        try {
            const response = await fetch(`/groups/saveNewMember/${groupId}`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                },
                body: formData
            });

            if (response.ok) {
                bootstrap.Modal.getInstance(addMemberModal).hide();
                location.reload();
            } else {
                const errorText = await response.text();
                const errorEl = document.getElementById("addMemberError");
                errorEl.textContent = "Failed to add member.";
                errorEl.style.display = "block";
                console.error("Server error:", errorText);
            }
        } catch (error) {
            console.error("Error adding member:", error);
        }
    });
});
