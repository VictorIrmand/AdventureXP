
export async function login(loginRequestDTO) {

    const response = await fetch("/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify(loginRequestDTO)
    });

    if (response.status === 403) {
        console.log("fejlet validation");
    }

    if (response.ok) {
        console.log("det virker");
    }
}

