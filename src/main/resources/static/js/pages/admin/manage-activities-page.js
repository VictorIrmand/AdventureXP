import {showGlobalNavbar} from "../../components/global-navbar.js";
import {navigate} from "../../utility/router.js";

export function mount() {
    /* language=HTML */
    document.querySelector("#app-main").innerHTML = `

        <div class="manage-activities-page">

            <div class="left"></div>

            <div class="center-right">


                <button class="create-activity-btn">
                    <i class="fa-solid fa-plus"></i>
                    Create Activity
                </button>


                <div class="edit-activities">
                    <p>Edit activities</p>

                </div>
                
            </div>


        </div>
    `

    showGlobalNavbar(document.querySelector(".left"));


    const createActivityBtn = document.querySelector(".create-activity-btn");
    createActivityBtn.addEventListener("click", e=> {
        e.preventDefault();
        navigate("/create-activity");
    })


    return () => {
        document.querySelector("#app-main").innerHTML = "";
    }
}