import React from "react";

export default function MovieDescription({movieData}) {
    return(<div className="video-description">
    <div style={{ textAlign: "center",backgroundColor: "white", borderRadius: "10px", border: "2px solid #61dafb", maxWidth: "600px" }}>{movieData.name}</div>
    <div>{movieData.description}</div>
    </div>);
}   