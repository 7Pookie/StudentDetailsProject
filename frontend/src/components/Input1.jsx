import React from "react";

const Input1 = ({ labelText, name, value, onChange, disabled }) => {
    return (
        <div className="input-group">
            <label htmlFor="first_name" className="block mb-2 text-sm font-semibold text-gray-900 dark:text-black">{labelText}</label>
            <input
                type="text"
                className="bg-gray-50 border-2 border-gray-300 text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-80 p-2.5 
                   dark:bg-slate-200 dark:border-gray-600 dark:placeholder-zinc-500 dark:text-black dark:caret-black dark:focus:ring-blue-500 dark:focus:border-blue-500 mt-2 mb-4"
                name={name} // Ensure the name prop is passed correctly
                value={value} // Ensure the value prop is passed correctly
                onChange={onChange} // Ensure the onChange handler is passed correctly
                disabled={disabled} // Ensure the disabled prop is respected
            />
        </div>
    );
};

export default Input1;