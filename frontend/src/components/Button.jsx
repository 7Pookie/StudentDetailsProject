import React from 'react';

const Button = ({ buttonText = "Details", onClick, type = "button", disabled }) => {
  return (
    <button className="px-4 py-2 w-auto min-w-[120px] max-w-[200px] text-white bg-black border border-black dark:border-white dark:bg-black dark:text-white rounded-lg font-bold transition-transform duration-300 hover:-translate-y-1 hover:shadow-md"
    type={type} // ✅ Ensures button type is explicitly controlled
            onClick={onClick}
            disabled={disabled}>
      {buttonText}
    </button>
  );
};

export default Button;
