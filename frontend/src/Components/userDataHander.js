export default async function userDataHandler(userData) {
  try {
    console.log(userData);
    const response = await fetch(
      "http://localhost:8080/choose-movie?" + new URLSearchParams(userData)
    );

    if (!response.ok) {
      throw new Error("Failed to fetch movie");
    }
    const resData = await response.json();
    console.log("Browser receive:", resData);
  } catch (error) {
    console.log(error);
  }
}
