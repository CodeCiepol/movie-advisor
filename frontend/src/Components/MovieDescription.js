import React from "react";

export default function MovieDescription({movieData}) {
    return(<div className="UserForm">
    <div>{movieData.name}</div>
    <div>{movieData.description}</div>
    </div>);
}