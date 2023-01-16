import Swal from "sweetalert2";

export function ErrorAlert(title: string, msg: string) {
  Swal.fire({
    title: "Error: " + title,
    heightAuto: false,
    text: msg,
  });
}

export function InfoAlert(title: string, msg: string, onComplete: () => void = () => {}) {
  Swal.fire({
    title: title,
    heightAuto: false,
    text: msg,
  }).then(onComplete);
}

export function ListAlert(title: string, msg: string, list: string[], onComplete: () => void = () => {}) {
  Swal.fire({
    title: title,
    heightAuto: true,
    html: `
      <span>${msg}:</span> 
      <ul>
        <li>
        ${list.join("</li><li>")}
        </li>
      <ul>
      `,
  }).then(onComplete);
}
