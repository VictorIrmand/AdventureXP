
export async function login(loginRequestDTO) {

    const response = await fetch("/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-type": "application/json",
        },
        body: JSON.stringify(loginRequestDTO)
    });

    if (response.status === 401) {
        console.log("fejlet validation");
    }

    if (response.ok) {
        console.log("det virker");
    }
}

export async function signUp(signUpRequestDTO) {
    const response = await fetch("/api/auth/signup", {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-type": "application/json",
        },
        body:JSON.stringify(signUpRequestDTO)
    });

    if(response.status === 400){
        console.log(await response.text())
    }

    if(response.ok) {
        console.log("Det virker")
    }
}



