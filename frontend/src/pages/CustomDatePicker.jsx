import React from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css"; // Import styles

const CustomDatePicker = ({ labelText, selectedDate, onChange }) => {
  return (
    <div className="flex flex-col">
      <label className="font-semibold mb-1">{labelText}</label>
      <DatePicker
        selected={selectedDate}
        onChange={onChange}
        dateFormat="yyyy-MM-dd" // Customize the date format
        className="border rounded-lg px-3 py-2 w-full"
        placeholderText="Select a date"
      />
    </div>
  );
};

export default CustomDatePicker;
