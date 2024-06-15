import React from "react";
import { useState } from "react";
import Header from "./Components/Header";
import UserForm from "./Components/UserForm";
import MovieDescription from "./Components/MovieDescription";
import "./App.css";
import LoadingIcon from "./Components/LoadingIcon";

function App() {
  const [movieIsFound, setMovieIsFound] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [movieData, setMovieData] = useState({name:'', description:''})
  return (
    <div className="App">
      <Header />
      <UserForm setMovieIsFound={setMovieIsFound} setMovieData={setMovieData} isLoading={isLoading} setIsLoading={setIsLoading} />
      {isLoading && <LoadingIcon/>}
      {movieIsFound && <MovieDescription movieData={movieData}/>}
    </div>
  );
}

export default App;
