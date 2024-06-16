import { useState } from "react";

import userDataHandler from "./userDataHander";

const UserForm = ({setMovieIsFound, setMovieData, isLoading, setIsLoading}) => {
  const [userMood, setUserMood] = useState("4");
  const [userGenre, setUserGenre] = useState("Drama");
  const [userWorkingDay, setuserWorkingDay] = useState(false);
 

  function submitHandler(event) {
    event.preventDefault();
    const userData = {
      mood: userMood,
      genre: userGenre,
      workingDay: userWorkingDay,
    };
    userDataHandler(userData, setMovieData, setMovieIsFound, setIsLoading);
  }

  return (
    <form className="UserForm" onSubmit={submitHandler}>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <label htmlFor="mood">How are you today?</label>
        <select
          id="mood"
          name="mood"
          onChange={(e) => {
            setUserMood(e.target.value);
          }}
          style={{ width: "5rem" }}
        >
          <option value="4">Happy</option>
          <option value="0">Sad</option>
          <option value="2">Neutral</option>
        </select>
      </div>

      <div
        style={{
          display: "flex",
          gap: "12px",
          justifyContent: "space-between",
        }}
      >
        <label htmlFor="genre">What is your best movie genre?</label>
        <select
          id="genre"
          name="genre"
          onChange={(e) => {
            setUserGenre(e.target.value);
          }}
          style={{ width: "5rem" }}
        >
          <option value="Drama">Drama</option>
          <option value="Comedy">Comedy</option>
          <option value="Action">Action</option>
        </select>
      </div>

      <div
        style={{
          display: "flex",
          gap: "12px",
          justifyContent: "space-between",
        }}
      >
        <label htmlFor="workingDay">Did you work today?</label>
        <div>
          <input
            type="checkbox"
            id="workingDay"
            name="workingDay"
            className="checkbox"
            style={{ marginRight: "4px" }}
            onChange={(e) => {
              setuserWorkingDay(e.target.checked);
            }}
          />
          <label htmlFor="workingDay">Worked</label>
        </div>
      </div>
      <button disabled={isLoading} className="button-submit-UserForm">{isLoading ? "Finding movie..." : "Find movie"}</button>
    </form>
  );
};
export default UserForm;
