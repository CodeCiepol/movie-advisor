export default async function userDataHandler(userData, setMovieData, setMovieIsFound, setIsLoading) {
  setIsLoading(true);
  try {
    const response = await fetch(
      "http://localhost:8080/findMovie?" + new URLSearchParams(userData)
    );

    if (!response.ok) {
      throw new Error("Failed to fetch movie");
    }
    const data = await response.json();
    console.log("Browser receive from server", data);
    setMovieData(data);
    setMovieIsFound(true);
  } catch (error) {
    console.log(error);
  }
  setIsLoading(false);
}

// dummy version to save money on chatgpt:
// export default async function userDataHandler(userData, setMovieData, setMovieIsFound) {
//     setMovieData({ name: "Interstellar", description: "You will appreciate the emotional depth and dramatic storyline of Interstellar after a long day at work." });
//     setMovieIsFound(true);
// }
