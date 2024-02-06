import "./button.scss";

export const Button = (props) => {
  const { color, text, minWidth, onClick } = props;

  return (
    <div
      className={`button ${
        color === "pink" ? "button--pink" : "button--green"
      }`}
      style={{ minWidth: minWidth }}
      onClick={onClick}
    >
      {text}
    </div>
  );
};
