import React from 'react'

const Input = ({labelText , placeholderText}) => {
  return (
    <div className='m-2'>
    <label htmlFor="first_name" className="block mb-2 text-sm font-semibold text-gray-900 dark:text-black">{labelText} *</label>
    <input 
        type="text" 
        id="first_name" 
        className="bg-gray-50 border-2 border-gray-300 text-black text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-80 p-2.5 
                   dark:bg-slate-200 dark:border-gray-600 dark:placeholder-zinc-500 dark:text-black dark:caret-black dark:focus:ring-blue-500 dark:focus:border-blue-500"
        placeholder={placeholderText} 
        required 
    />
</div>

  )
}

export default Input