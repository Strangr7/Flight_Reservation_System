const DatePickerManager = {
    initialize: function() {
        console.log("Initializing date pickers");

        flatpickr("#departureDate", {
            dateFormat: "Y-m-d",
            altInput: true,
            altFormat: "F j, Y",
            weekNumbers: true,
           
            defaultDate: new Date().setDate(new Date().getDate() + 1),
        });

        flatpickr("#returnDate", {
            dateFormat: "Y-m-d",
            altInput: true,
            altFormat: "F j, Y",
            minDate: "today",
            weekNumbers: true,
            disable: [
                function(date) {
                    const departureDate = document.getElementById("departureDate").value;
                    return departureDate && date < new Date(departureDate);
                },
            ],
        });

        return Promise.resolve();
    }
};