import { useState } from "react";

import userDataHandler from "./userDataHander";

const UserForm = ({
  setMovieIsFound,
  setMovieData,
  isLoading,
  setIsLoading,
}) => {
  const [userMood, setUserMood] = useState("2");
  const [userGenre, setUserGenre] = useState("Drama");
  const [userWorkingDay, setuserWorkingDay] = useState(false);
  const [isFocusOnFavouriteGenre, setIsFocusOnFavouriteGenre] = useState(true);

  function submitHandler(event) {
    event.preventDefault();
    const userData = {
      mood: userMood,
      genre: userGenre,
      workingDay: userWorkingDay,
      isFocusOnFavouriteGenre: isFocusOnFavouriteGenre,
    };
    userDataHandler(userData, setMovieData, setMovieIsFound, setIsLoading);
  }

  return (
    <form className="UserForm" onSubmit={submitHandler}>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <label htmlFor="mood">How are you today?</label>
        <div className="range-container">
          <input
            type="range"
            id="mood"
            name="mood"
            min="0"
            max="4"
            list="values"
            defaultValue={userMood}
            onChange={(e) => {
              setUserMood(e.target.value);
            }}
            // style={{ width: "6rem" }}
          />
          <datalist id="values">
            <option value="0" label="Sad&nbsp;&nbsp;&nbsp;"></option>
            <option value="1" label=""></option>
            <option value="2" label="Neutral"></option>
            <option value="3" label=""></option>
            <option value="4" label="Happy"></option>
          </datalist>
        </div>
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
          style={{ width: "7rem" }}
        >
          <option value="Drama">Drama</option>
          <option value="Comedy">Comedy</option>
          <option value="Action">Action</option>
          <option value="Science Fiction">Science Fiction</option>
          <option value="Adventure">Adventure</option>
          <option value="Crime">Crime</option>
          <option value="Thriller">Thriller</option>
          <option value="Fantasy">Fantasy</option>
          <option value="Horror">Horror</option>
          <option value="Animation">Animation</option>
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
          <label htmlFor="workingDay">
            {userWorkingDay ? "worked" : "didn't work"}
          </label>
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
        </div>
      </div>

      <div
        style={{
          display: "flex",
          gap: "12px",
          justifyContent: "space-between",
        }}
      >
        <label htmlFor="isFocusOnFavouriteGenre">
          Do you want to focus on your genre?
        </label>
        <div>
          <input
            type="checkbox"
            id="isFocusOnFavouriteGenre"
            name="isFocusOnFavouriteGenre"
            className="checkbox"
            style={{ marginRight: "4px" }}
            checked={isFocusOnFavouriteGenre}
            onChange={(e) => {
              setIsFocusOnFavouriteGenre(e.target.checked);
            }}
          />
          <label htmlFor="isFocusOnFavouriteGenre"></label>
        </div>
      </div>
      <button disabled={isLoading} className="button-submit-UserForm">
        {isLoading ? "Finding movie..." : "Find movie"}
      </button>
    </form>
  );
};
export default UserForm;
